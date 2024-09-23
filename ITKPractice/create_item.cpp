/*= ============================================================================
Copyright(c) 2003 - 2005 XXXXXX
Unpublished - All Rights Reserved
============================================================================== =
File description :

Filename: create_item.cpp
Module : main

This program contains all the functions that are used to create an item.

USAGE : create_item[-u = <user_id> -p = <password>[-g = <group>]]
- item_id = <new_item_id>
[-item_name = <new_item_name>]
[-item_type = <new_item_type>]
[-item_initial_revision = <new_item_initial_revision>]
[-h]
* /

/****************************************************************************/
/*                      I N C L U D E   F I L E S                           */
/****************************************************************************/

#include <sys/types.h>
#include <sys/stat.h>
#include <stdlib.h>

#include <unidefs.h>


#include <tc/tc_util.h>
#include <tccore/item.h>
#include <tccore/workspaceobject.h>
#include <tcinit/tcinit.h>
#include<tccore/aom_prop.h>
#include<tccore/aom.h>

#include <base_utils/Mem.h>
#include <fclasses/tc_date.h>
#include <fclasses/tc_string.h>
#include <nls/nls.h>
#include <pom/pom/pom.h>
#include <ps/ps.h>
#include <string.h>
#include <tc/emh.h>




#define LOGOUT_AND_RETURN() { ITK_exit_module(true); return EXIT_FAILURE; }

/*****************************************************************************
FUNCTION PROTOTYPES
*****************************************************************************/
/**
   Retrieves the error message associated with the top-most error on the error stack.
 */
static void get_top_most_error_string(char** error_message   /* The associated error message.
                                                                 Call MEM_free on the retrieved pointer. */
);


static void display_help(void);
static logical validate_arguments_name(int argc, char* argv[]);


/****************************************************************************/
/*                                                                          */
/*  Function Name:   ITK_user_main                                          */
/*                                                                          */
/*  Program ID:      find_released_item_rev_itk_main.c                      */
/*                                                                          */
/*  Description:     This is the main function which calls all the other    */
/*                   functions.                                             */
/*                                                                          */
/*  Parameters:      char*  argv[] <I>  - Command Line Arguments            */
/*                   int    argc   <I>  - No of command line arguments.     */
/*                                                                          */
/*  Return Value:    0 - on Success                                         */
/*                   1 - on Failure                                         */
/*                                                                          */
/*  Special Logic Notes:  None                                              */
/*                                                                          */
/****************************************************************************/

int ITK_user_main(int argc, char* argv[]) {

    int       retcode = ITK_ok;  /* Function return code */
    int       no_of_item_revs = 0;       /* Number of item revisions */
    int       end_date_flag = 0;       /* End date flag */

    char* from_date = NULL;    /* From date */
    char* end_date = NULL;    /* End date */
    char* out_file_name = NULL;    /* Output file name */
    char* object_type = NULL;    /* Item Revision Type */

    logical   date_is_valid = FALSE;   /* Date Validation */
    logical   key_out_format = FALSE;   /* Output in key format */

    date_t    from_dt = NULLDATE; /* From date in date struct */
    date_t    end_dt = NULLDATE; /* end date in date struct */

    tag_t item_tag = NULLTAG;
    tag_t item_rev_tag = NULLTAG;

    tag_t* item_rev_tags = NULL;     /* Item Revision tags */
    FILE* out_file_fp = NULL;     /* Output file pointer */


    (void)argc;
    (void)argv;
    //----------------------------- DISPLAY HELP -------------------------//
        /* Display help if user asked for it */
    if (ITK_ask_cli_argument("-h")) {//if 1		
        TC_printf(" Displaying Help... \n");
        display_help();
        return EXIT_SUCCESS;
    }// if 1

//----------------------------- Validate the arguments -------------------------//
    TC_printf("Validating the arguments... \n");
    //validating the arguments names specified through command line
    //If any of the argumnent name is invalid then user can not proceed
    if (validate_arguments_name(argc, argv) == false) {
        display_help();
        return EXIT_FAILURE;
    }

    //----------------------------- Get the Inputs -------------------------//
    TC_printf("Get the Object Types... \n");
    /* Get the object type if given otherwise default it NULL */
    object_type = ITK_ask_cli_argument("-obj_type=");

    //----------------------------- Get the user logged in-------------------------//
    retcode = ITK_auto_login();
    if (retcode != ITK_ok) {
        TC_printf("Failed to login to Teamcenter using auto-login with ");
        TC_printf("error code %d\n\n", retcode);
        return EXIT_FAILURE;
    }
    else {
        TC_printf("Successfully Logged into Teamcenter \n");
    }

    //----------------------------- Create Item-------------------------//	
    TC_printf("Creating Item \n");
    retcode = ITEM_create_item(NULL, "MyItem", "Item", "A", &item_tag, &item_rev_tag);
    TC_printf("ITEM_create_item Ret Code %d \n", retcode);
    //----------------------------- Save Item-------------------------//	
    AOM_save_without_extensions(item_tag);
    TC_printf("Saving the Item \n");





    /* Cleanup */
    MEM_free(item_rev_tags);
    retcode = ITK_exit_module(true);
    return EXIT_SUCCESS;


}//main





/****************************************************************************/
/*                                                                          */
/*  Function Name:   display_help                                           */
/*                                                                          */
/*  Program ID:      find_released_item_rev_itk_main.c                      */
/*                                                                          */
/*  Description:     This function displays the help on this utility to the */
/*                   standard output.                                       */
/*                                                                          */
/*  Return Value:    None                                                   */
/*                                                                          */
/*  Special Logic Notes:  None                                              */
/*                                                                          */
/****************************************************************************/

static void display_help(void) {//display help
    TC_fprintf(stdout, "\n");
    TC_fprintf(stdout, "\nUsage:\n\n");
    TC_fprintf(stdout, "create_item \n\t[-u=<user_id>] [{-p=<password> | -pf=<password file>}]\n");
    TC_fprintf(stdout, "\t[-g=<group] -item_id=<new_item_id\n");
    TC_fprintf(stdout, "\t[-item_name=<new_item_name>]\n");
    TC_fprintf(stdout, "\t[-item_type=<new_item_type>]\n");
    TC_fprintf(stdout, "\t[-h]\n");

    TC_fprintf(stdout, "\nWhere:\n\n");
    TC_fprintf(stdout, "     -u        - Teamcenter user ID. If -u= is used without\n");
    TC_fprintf(stdout, "                 providing the <user_id> value, then\n");
    TC_fprintf(stdout, "                 operating system username is used as\n");
    TC_fprintf(stdout, "                 the user ID\n\n");

    TC_fprintf(stdout, "     -p        - Teamcenter password associated with the\n");
    TC_fprintf(stdout, "                 -u=<user_id>. This is optional. If\n");
    TC_fprintf(stdout, "                 -p= is used without providing the\n");
    TC_fprintf(stdout, "                 <password> value, then a NULL value\n");
    TC_fprintf(stdout, "                 is used as password. If this is not\n");
    TC_fprintf(stdout, "                 specified, then the <user_id> value\n");
    TC_fprintf(stdout, "                 is used as password\n\n");

    TC_fprintf(stdout, "     -pf       - Teamcenter password in file.\n\n");
    TC_fprintf(stdout, "     -g        - Teamcenter group associated with the \n");
    TC_fprintf(stdout, "                 <user_id>. If this option is not\n");
    TC_fprintf(stdout, "                 specified, then the default Teamcenter\n");
    TC_fprintf(stdout, "                 group is used as user group\n\n");

    TC_fprintf(stdout, "     -item_id - ID of the new Item, the\n");
    TC_fprintf(stdout, "     -item_name - Name of the new Item, the\n");
    TC_fprintf(stdout, "     -item_type - Type of the new Item, the\n");

    TC_fprintf(stdout, "     -h        - Displays help\n\n");
}//display help




/****************************************************************************/
/*                                                                          */
/*  Function Name:   validate_arguments_name                                */
/*                                                                          */
/*  Program ID:      find_released_item_rev_itk_main.c                      */
/*                                                                          */
/*  Description:     This function validates the argument supplied by the   */
/*                   user.  User will pass the argument like -u=infodba     */
/*                   -p=infodba etc this function loops over each argument  */
/*                   extract the argument name ex* -u and compare it        */
/*                   with the valid argument supported by the utility.      */
/*                   It will return true if all arguments are valid         */
/*                   otherwise it will return false                         */
/*  Parameters:      int    argc     <I>    number of argument              */
/*                   char   argv     <I>    array of arguments              */
/*                                                                          */
/*                                                                          */
/*  Return Value:    logical  (true/false)                                  */
/*                   true - all arguments are valid                         */
/*                   false - one or more arguments are invalid              */
/*                                                                          */
/*  Special Logic Notes:  None                                              */
/*                                                                          */
/****************************************************************************/

static logical validate_arguments_name(int argc, char* argv[]) {//validate arguments
    int index = 0;
    char  arg_name[255];
    char* arg_value;
    int arg_name_length;
    logical is_arg_valid = false;
    int index1 = 0;
    char* valid_arg_list[] = { "-u", "-p", "-pf", "-g", "-item_id", "-item_name", "-item_type" };
    int valid_args = sizeof(valid_arg_list) / sizeof(char*);
    logical is_all_arg_valid = true;

    //comparing the passed argument to the valild argument supported by the utility
    //we are skipping the first argument because it is name of the executable
    for (index = 1; index < argc; index++)
    {
        is_arg_valid = false;
        arg_name_length = 0;
        //argument will in form of -argname=value ex. -u=infodba
        arg_value = strchr(argv[index], '=');

        if (arg_value != NULL)
        {
            arg_name_length = tc_strlen(argv[index]) - tc_strlen(arg_value);
        }
        //if argument do not contains '=' then it is the invalid argument
        //if the argument name length is larger than 254 then also it is invalid 
        if (arg_name_length >= 255 || arg_value == NULL)
        {
            is_all_arg_valid = false;
            TC_fprintf(stdout, "\nError : The argument <%s> specified through command line is not recognized.\n", argv[index]);
            continue;
        }
        //getting the name of argument example in "-u=infodba" in this argument name will be "-u"
        tc_strncpy(arg_name, argv[index], arg_name_length);
        arg_name[arg_name_length] = '\0';

        //comparing the argument name to the valid argument name supported by this utility
        for (index1 = 0; index1 < valid_args; index1++)
        {
            if (tc_strcasecmp(valid_arg_list[index1], arg_name) == 0)
            {
                is_arg_valid = true;
                break;
            }
        }
        //if argument is invalid giving error to user
        if (is_arg_valid == false)
        {
            is_all_arg_valid = false;
            TC_fprintf(stdout, "\nError : The argument <%s> specified through command line is not recognized.\n", argv[index]);
        }
    }

    return is_all_arg_valid;
}//validate arguments

