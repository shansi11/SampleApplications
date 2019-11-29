import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Future;

import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.KeyVaultClientService;
import com.microsoft.azure.keyvault.KeyVaultConfiguration;
import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;
import com.microsoft.azure.keyvault.models.Secret;
import com.microsoft.azure.keyvault.models.SecretIdentifier;
import com.microsoft.windowsazure.Configuration;


public class EstablishDBConnectionUsingVault{



	public EstablishDBConnectionUsingVault() 
	{
		try { 

			Connection con = null;
			KeyVaultCredentials kvCred = new ClientSecretKeyVaultCredential("<APPLICATION ID>","<APPLICATION SECRET>");
			Configuration config = KeyVaultConfiguration.configure(null, kvCred);
			KeyVaultClient vc = KeyVaultClientService.create(config); 
			
			String jdbcDriver = getSecretFromVault("<URL>",vc);           

			String databaseURL = getSecretFromVault("<URL>",vc);
			//databaseURL = databaseURL.replace("\"", ""); ** CHECK IF THIS IS NEEDED FOR POSTGRES ************************

			String username = getSecretFromVault("<URL>",vc);	                

			String password = getSecretFromVault("<URL>",vc);      

			String dbName = getSecretFromVault("<URL>",vc);


			try {
				Class.forName(jdbcDriver).newInstance();
				con = DriverManager.getConnection(databaseURL);
				if(con != null) 
					System.out.println("Database Connection Established");
				else 
					System.out.println("Database Connection Not Established. Check the credentials");
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				con.close();
			}
		}
			catch(SQLException sqlex) {
				sqlex.printStackTrace();
			}
		}

		private String getSecretFromVault(String keyIdentifier,KeyVaultClient vc) {
			String secretValue = null;
			try {
				Future<Secret> result =  vc.getSecretAsync(keyIdentifier);

				SecretIdentifier secretIdentifier = result.get().getSecretIdentifier();
				secretValue = result.get().getValue();
				//System.out.println("1111111111111111111111111111111 "+vc.getSecretAsync("https://cargalinuxkeyvault.vault.azure.net/secrets/databaseURL").get());
			}catch (Exception e) {
				e.printStackTrace();
				//exception = e.getMessage();

			}   
			return secretValue;
		}


		public static void main(String args[]) {
			new EstablishDBConnectionUsingVault();
		}
	}
