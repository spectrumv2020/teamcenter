================================================================================
Copyright 2021.
Siemens Product Lifecycle Management Software Inc.
All Rights Reserved.

Document: how_to_deploy_using_these_configurations_readme.txt
	
================================================================================


Contents:

  Introduction
  Quick Deploy Configuration Examples
  Preparation 
  Common Deployment Instructions
  Specific Deployment Instructions


Introduction

	This readme briefly provides instructions to configure and deploy Reference Architectures 
	documented in the Teamcenter Deployment Reference Architecture document using Quick Deploy 
	Configurations shipped part of Teamcenter Deployment Reference Architecture downloads.

Quick Deploy Configuration Examples
	Quick Deploy Configuration examples for all the Teamcenter Deployment Reference Architecture are 
	shipped part of Teamcenter Deployment Reference Architecture downloads and can be located under the folder
	\quick_deploy_configurations\wntx64 or \quick_deploy_configurations\lnx64 depending on Operating System.


Preparation
	This section provides the one time preparation required before starting the deployment of Reference Architectures.
	
	Step1: Download Deployment Center 4.2 kit from GTAC, Unzip and Install Deployment Center [Refer the Deployment Center Help Guide for the detailed instructions] 
	Step2: Download and copy Teamcenter 13.3, Active Workspace 6.0 and Micro Service Framework 6.0 into Software Repository 
	Step3: Locate and unzip dc_quick_deploy.zip file in the folder <Deployment Center 4.2 kit>\additional_tools\Teamcenter\dc_quick_deploy 
	Step4: Start command line shell as administrator and navigate to <Deployment Center 4.2 kit>\additional_tools\Teamcenter\dc_quick_deploy

Common Deployment Instructions

	Follow the instructions given below to configure and deploy the Deployment Reference Architecture documented. 
	These instructions are common to all the Deployment Reference Architecture and any specific instructions are 
	covered in the Specific Deployment Instructions section.
	
	Step1: Locate and Open the Quick Deploy Configuration example under the folder \quick_deploy_configurations\wntx64 or \quick_deploy_configurations\lnx64 depending on Operating System that you would like to deploy, perform following edits and save. 
		Search and replace value for the "machineName", refer the Keys section mentioned above to replace the machine appropriately based on the tier that belongs to.
		Search and replace "REPLACEME" with appropriate value.
		Ensure to review all the default values specified in the Quick Deploy Configuration example and modify as needed.
			
	Step2: From <Deployment Center 4.2 kit>\additional_tools\Teamcenter\dc_quick_deploy command line, Locate and run dc_quick_deploy.bat in case of Windows/dc_quick_deploy.sh in case of Linux with following arguments. Run dc_quick_deploy -h for more details on utility and arguments.
		dc_quick_deploy.bat -environment=<Name of the environment> -inputFile=<specify absolute file path of Deployment Reference Architecture Quick Deploy Configuration Example edited in the Step1> -dcusername=<The deployment center username.> -dcpassword=<The deployment center password.> -dcUrl=<The deployment center url that is up and running>
	
	Optional Step: Login to Deployment Center and review interactively for accuracy.  
	
	Step3: If there are no specific instructions mentioned for the example then locate the generated deployment script for the specific target machine for the specified environment and execute the deployment script [Refer the Deployment Center Help Guide for the detailed instructions].
