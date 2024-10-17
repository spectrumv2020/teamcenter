

package com.teamcenter.hello;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.teamcenter.clientx.AppXSession;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.strong.core.DataManagementService;
import com.teamcenter.services.strong.core.EnvelopeService;
import com.teamcenter.services.strong.core.FileManagementService;
import com.teamcenter.services.strong.core.SessionService;
import com.teamcenter.services.strong.core._2006_03.DataManagement.CreateDatasetsOutput;
import com.teamcenter.services.strong.core._2006_03.DataManagement.CreateDatasetsResponse;
import com.teamcenter.services.strong.core._2006_03.FileManagement.DatasetFileInfo;
import com.teamcenter.services.strong.core._2006_03.FileManagement.DatasetFileTicketInfo;
import com.teamcenter.services.strong.core._2006_03.FileManagement.GetDatasetWriteTicketsInputData;
import com.teamcenter.services.strong.core._2006_03.FileManagement.GetDatasetWriteTicketsResponse;
import com.teamcenter.services.strong.core._2008_06.DataManagement.DatasetProperties2;
import com.teamcenter.soa.client.Connection;
import com.teamcenter.soa.client.FileManagementUtility;
import com.teamcenter.soa.client.model.ErrorStack;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.ServiceData;
import com.teamcenter.soa.client.model.strong.Dataset;
import com.teamcenter.soa.client.model.strong.Envelope;
import com.teamcenter.soa.client.model.strong.Person;
import com.teamcenter.soa.client.model.strong.User;

public class EnvelopeSend 
{
	private EnvelopeService envService;
	private DataManagementService dmService;
	private SessionService session;

	private User user;
	private Envelope envelope;
	private Dataset dataset;

	public EnvelopeSend(Connection connection) throws ServiceException
	{
		dmService = DataManagementService.getService(connection);
		envService = EnvelopeService.getService(connection);
		session = SessionService.getService(connection);

		user = session.getTCSessionInfo().user;
	}

	public boolean createEnvelope(String filePath, String filename, String eBOM)
	{
		try
		{
			DataManagementService.CreateIn input = new DataManagementService.CreateIn();
			dataset = null;
			input.clientId = "ID:" + input.hashCode();
			input.data = new DataManagementService.CreateInput();
			input.data.boName = "Envelope"; 
			input.data.stringProps.put("object_name", "LegacyBOM Import Report for Assembly - "+eBOM); // Subject
			input.data.stringProps.put("object_desc", "This is an auto-generated email from Teamcenter");
			//input.data.tagArrayProps.put("listOfReceivers", new ModelObject[]{ user }); 

			String[] uids = { user.get_person().getUid() };
			ServiceData data = dmService.loadObjects(uids);
			Person person = (Person)data.getPlainObject(0);
			input.data.stringArrayProps.put("fnd0ListOfExtRecipients",new String[] {person.get_PA9()}  ); 
			// To: receivers (User objects)
			dataset = createDataset(filePath, filename);
			input.data.tagArrayProps.put("contents", new ModelObject[]{ dataset});
			DataManagementService.CreateResponse resp = dmService.createObjects(new DataManagementService.CreateIn[]{ input });

			if(!ServiceDataError(resp.serviceData))
			{
				for(DataManagementService.CreateOut out : resp.output)
				{
					for(ModelObject obj : out.objects)
					{
						if(obj instanceof Envelope)
						{
							envelope =(Envelope)obj;
							return true;
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

	private Dataset createDataset(String filePath, String filename) {
		DatasetProperties2[] datasets = new DatasetProperties2[1];
		DatasetProperties2 dataProp = new DatasetProperties2();

		dataProp.clientId = "DatasetID"+ dataProp.hashCode();
		dataProp.name = "Execute Log";
		dataProp.description = "Execute Log of legacy BOM Tool";
		dataProp.type = "Text";
		dataProp.toolUsed = "TextEditor";
		// dataProp.relationType
		dataProp.container = null;
		datasets[0]=dataProp;

		CreateDatasetsResponse datasetResp = dmService.createDatasets2(datasets);
		if(!ServiceDataError(datasetResp.serviceData))
		{
			for(CreateDatasetsOutput out : datasetResp.output)
			{
				Dataset datasetObject = out.dataset;
				if(datasetObject!=null) {
					UploadNamedReference(datasetObject, filePath, filename);
					return datasetObject;
				}


			}
		}
		return null;
	}

	public void sendEnvelope()
	{
		ServiceData sdata = envService.sendAndDeleteEnvelopes(new Envelope[]{ envelope });

		ServiceDataError(sdata);
		if(dataset!=null)
			dmService.deleteObjects(new ModelObject[] {dataset});
	}

	protected boolean ServiceDataError(final ServiceData data)
	{
		if(data.sizeOfPartialErrors() > 0)
		{
			for(int i = 0; i < data.sizeOfPartialErrors(); i++)
			{
				for(String msg : data.getPartialError(i).getMessages())
					System.out.println(msg);
			}

			return true;
		}

		return false;
	}


	public boolean UploadNamedReference(Dataset dataset, String filePath, String fileName)
	{

		String tempDir = System.getProperty("java.io.tmpdir");

		FileManagementService fmservice = FileManagementService.getService(AppXSession.getConnection());
		FileManagementUtility  fileUtility = new FileManagementUtility(AppXSession.getConnection());

		GetDatasetWriteTicketsInputData input = new GetDatasetWriteTicketsInputData();
		DatasetFileInfo[] fileInfo = new DatasetFileInfo[1];
		//String fileName = "body"+input.hashCode()+".txt";
		//String filePath =tempDir + fileName;
		//Boolean success = createFile(filePath, fileContent);
		if(!filePath.isEmpty()) {
			fileInfo[0] = new DatasetFileInfo();
			fileInfo[0].clientId = "textfileInfo"+input.hashCode();
			fileInfo[0].allowReplace = true;
			fileInfo[0].fileName = fileName;
			fileInfo[0].isText = true;
			fileInfo[0].namedReferencedName = "Text";



			input.createNewVersion = true;
			input.dataset = dataset;
			input.datasetFileInfos = fileInfo;


			GetDatasetWriteTicketsResponse ticketResp = fmservice.getDatasetWriteTickets(new GetDatasetWriteTicketsInputData[]{ input });


			if(!ServiceDataError(ticketResp.serviceData))
			{
				for(DatasetFileTicketInfo ticketInfo : ticketResp.commitInfo[0].datasetFileTicketInfos)
				{
					String ticket = ticketInfo.ticket;
					ErrorStack err = fileUtility.putFileViaTicket(ticket, new File(filePath));

					if(err == null)
					{
						ServiceData sdata = fmservice.commitDatasetFiles(ticketResp.commitInfo);

						if(ServiceDataError(sdata))
							return false;
					}
					else
					{
						ErrorStackError(err);
						return false;
					}
				}

				return true;
			}
		}

		return false;
	}


	private Boolean createFile(String fileName, String fileContent) {
		try (BufferedWriter writer = new BufferedWriter( new FileWriter(fileName))) {
			writer.write(fileContent);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected boolean ErrorStackError(final ErrorStack err)
	{
		if(err.getMessages().length > 0)
		{
			for(String msg : err.getMessages())
				System.out.println(msg);


			return true;
		}


		return false;
	}
}