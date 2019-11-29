import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;
import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;
import com.microsoft.windowsazure.core.pipeline.filter.ServiceRequestContext;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.Header;


public class AzureKeyVaultCredential extends KeyVaultCredentials {
    private static final long DEFAULT_TOKEN_ACQUIRE_TIMEOUT_IN_SECONDS = 60L;
    private String clientId;
    private String clientKey;
    private long timeoutInSeconds;

    public AzureKeyVaultCredential(String clientId, String clientKey, long timeoutInSeconds) {
        this.clientId = clientId;
        this.clientKey = clientKey;
        this.timeoutInSeconds = timeoutInSeconds;
    }

    public AzureKeyVaultCredential(String clientId, String clientKey) {
        this(clientId, clientKey, DEFAULT_TOKEN_ACQUIRE_TIMEOUT_IN_SECONDS);
    }

    public String doAuthenticate(String authorization, String resource, String scope) {
        AuthenticationContext context = null;
        AuthenticationResult result = null;
        String token = "";
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            context = new AuthenticationContext(authorization, false, executorService);
            final ClientCredential credential = new ClientCredential(this.clientId, this.clientKey);

            final Future<AuthenticationResult> future = context.acquireToken(resource, credential, null);
            result = future.get(timeoutInSeconds, TimeUnit.SECONDS);
            token = result.getAccessToken();
        } catch (MalformedURLException muex) {
            throw new RuntimeException(muex.getMessage());
        } 
        catch ( TimeoutException toex) {
            throw new RuntimeException(toex.getMessage());
        } 
        catch (InterruptedException ieex) {
            throw new RuntimeException(ieex.getMessage());
        } 
        catch (ExecutionException ex) {
            throw new RuntimeException(ex.getMessage());
        } 
        finally {
            executorService.shutdown();
        }
        return token;
    }

	@Override
	public Header doAuthenticate(ServiceRequestContext arg0, Map<String, String> arg1) {
		// TODO Auto-generated method stub
		return null;
	}
}