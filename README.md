# DataExtractor 
This repo is for Data Extractor project

# Build Mechanism 

## Pre-requisites
1. Apach-ant (Will be provided with this package if needed)

## Build Process
This project uses apache-ant to build and create libraries and executables
To build on Windows
1. Change directory to scripts under DataExtractor
2. Update environment variables in build_env.bat and save
3. Launch a command shell 
4. source environment 
5. run "ant clean" and "ant" - This generates all binaries required
6. run "ant deploy" - This copies all required binaries to DEPLOY_ROOT/bin directory

## Package Process
1. Zip all the files in DEPLOY_ROOT/bin
2. Copy on to server/host where the DataExtractor program need to be executed

## Deploy
1. It is optional to copy these binaries into TC_ROOT/bin - But, not necessary

# Running the program
## Pre-Requisites
1. Teamcenter 2-Tier environment (Command Shell)

## Extracting MCAD Data
1. Launch a Teamcenter shell
2. Change directory to where the package is extracted
3. Run command - ItemIdExtractor -u=<userid> -p=<password> -g=<group> -item_type=<Item Type> -outfile=<output file name>
   Example: ItemIdExtractor -u=a490900 -p=a490900 -g=dba -item_type=TR4_PN -outfile=output.txt
4. The output from previous command may contain large number of items for a given type. Split item ids into smaller files
   and provide each file as input to next command(MCADDataExtractor)
5. Run command - MCADDataExtractor -u=<userid> -p=<password> -g=<group> -infile=<Input file name> -dataset_types=<types of datasets to extract files for> -outfile=<output file name>
   Example: MCADDataExtractor -u=a490900 -p=a490900 -g=dba -infile=PN1.txt -dataset_types=ProPrt,ProAsm,ProDrw -outfile=PN1_MCAD.txt
   (Note: Dataset attached to Items will not extracted)

