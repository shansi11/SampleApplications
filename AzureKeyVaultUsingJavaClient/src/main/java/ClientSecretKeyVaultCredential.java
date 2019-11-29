import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;
import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;
import com.microsoft.windowsazure.core.pipeline.filter.ServiceRequestContext;

/**
 * Based on example from Microsoft documentation:
 * https://azure.github.io/azure-sdk-for-java/com/microsoft/azure/keyvault/authentication/KeyVaultCredentials.html
 */
public class ClientSecretKeyVaultCredential extends KeyVaultCredentials
{
 private String applicationId ;
 private String applicationSecret;
 
 public ClientSecretKeyVaultCredential(String applicationId, String applicationSecret)
 {
	 
     this.setApplicationId(applicationId);
     this.setApplicationSecret(applicationSecret);
 }
 

	public String getApplicationId() {
		return applicationId;
	}

	private void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getApplicationSecret() {
		return applicationSecret;
	}

	private void setApplicationSecret(String applicationSecret) {
		this.applicationSecret = applicationSecret;
	}

 @Override
 public Header doAuthenticate(ServiceRequestContext request, Map<String, String> challenge) {
	 
	 
     AuthenticationResult res = null;
     String authorization = challenge.get("authorization");
     String resource = challenge.get("resource");

     
     try {
         res = GetAccessToken(authorization, resource);
     } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
     }

     return new BasicHeader("Authorization", res.getAccessTokenType() + " " + res.getAccessToken());
 }
 
 private AuthenticationResult GetAccessToken(String authorization, String resource)
         throws InterruptedException, ExecutionException {
	 
	 
     AuthenticationContext ctx = null;
     AuthenticationResult res = null;
     ExecutorService service = Executors.newFixedThreadPool(1);
     System.out.println("resp******************** 11");
     try {
         ctx = new AuthenticationContext(authorization, false, service);
     } catch (MalformedURLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
     }
     Future<AuthenticationResult> resp = ctx.acquireToken(resource, new ClientCredential(
             this.getApplicationId(), this.getApplicationSecret()), null);
     res = resp.get();
     System.out.println("resp******************** "+resp.get().getAccessToken().length());
     service.shutdown();
     
     return res;
 }
}
