package adLdap;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class OutsideDomain 
{
	
//	https://www.forumsys.com/tutorials/integration-how-to/ldap/online-ldap-test-server/
		
	private static final String contextFactory = "com.sun.jndi.ldap.LdapCtxFactory";	
	private static  String  ldapUsername =  "cn=read-only-admin,dc=example,dc=com"; 
	private static  String  ldapPassword =  "password";  
	private static  String  servername   =  "ldap://ldap.forumsys.com:389";
	private static  String  searchbase   =  "ou=mathematicians,dc=example,dc=com"; 
	private static  String  searchfilter =  "ou=mathematicians";  //"mathematicians/scientists";
	
	
	
	public OutsideDomain() 
	{
		  ArrayList members = new ArrayList();
	  	  
		  Hashtable <String, Object> env = new Hashtable<String, Object>();
		  env.put(Context.SECURITY_AUTHENTICATION, "simple");
		  env.put(Context.SECURITY_PRINCIPAL, ldapUsername);
		  env.put(Context.SECURITY_CREDENTIALS, ldapPassword);
		  env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
		  env.put(Context.PROVIDER_URL, servername);

		// print error/output
		  env.put("com.sun.jndi.ldap.trace.ber", System.out);  // .err / .out

		  LdapContext ctx;
		  try
		  {

		   ctx = new InitialLdapContext(env, null);
		   
	
		   
		   NamingEnumeration results = null;
		   
		   try
		   {
		    SearchControls controls = new SearchControls();
		    controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		    results = ctx.search(searchbase, searchfilter, controls);
	
		    System.out.println("results == " + results) ;
		    
		    while (results.hasMore())
		    {
		     SearchResult searchResult = (SearchResult) results.next();
		     Attributes attributes = searchResult.getAttributes();
		     Attribute attr = attributes.get("uniqueMember"); //("name"); //("uniqueMember");
		     
		     System.out.println("\n attr.size() === " + attr.size()) ;
		     
		     for (int i=0;i < attr.size();i++)
		     {
		      members.add((String) attr.get(i));
		      System.out.println("#"+i+ "  attr.get(i) ===" + attr.get(i));
		     }
		    }
		   }
		   catch (NameNotFoundException e)
		   {
		  	 System.out.println("catch 1 - The base context was not found. Just clean up and exit.");
		    // The base context was not found.
		    // Just clean up and exit.
		   }
		   catch (NamingException e)
		   {
		  	 System.out.println("catch 2 ");
		    throw new RuntimeException(e);
		   }
		   finally
		   {
		    if (results != null)
		    {
		     try
		     {
		      results.close();
		     }
		     catch (Exception e)
		     {
		      // Never mind this.
		    	 System.out.println("finally - catch 1 -exit");
		     }
		    }
		    
		    if (ctx == null)
		    {
		    	 System.out.println("catch finaly 2 - ctx is null");
		    }
		    
		    if (ctx != null)
		    {
		     try
		     {
		      ctx.close();
		     }
		     catch (Exception e)
		     {
		    	 System.out.println("catch finaly 3 - ctx is not null");
		    	 System.out.println("catch >>>> "+e.getMessage() );
		     }
		    }
		    
		    
		   }
	
		   System.out.println("Print data ");
		   // Loop through the memebers of the and print them out
		   for (int i = 0; i < members.size(); i++)
		   {
		    System.out.println(members.get(i));
		   }
		  }
		  catch (NamingException e1)
		  {
		   // TODO Auto-generated catch block
		   e1.printStackTrace();
		  }
		
	}

	
}
