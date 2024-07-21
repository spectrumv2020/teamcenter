/* Get Item Revision Status Tag*/
#include <stdlib.h>
#include <tc/tc.h>
#include <tccore/item.h>
#include <tccore/aom_prop.h>
#include <tccore/aom.h>
#include <tc/workspaceobject.h>

#include <ae/ae.h>
#include <ae/dataset.h>
#include <epm/epm.h>
#include <property/prop.h>
#include <pom/pom.h>
#include <sa/tcfile.h>
#include <ss/ss_const.h>
#include <user_exits/user_exits.h>

#define ITK_CALL(x){	//define ITK_Call										\

int status;																		\
char* error_string;																\
if (status = (x)) != ITK_OK){//if not ITK_OK									\
		EMH_get_error_string(NULLTAG, status, &error_string);						\
		printf("ERROR: %d ERROR MESSAGE: %s. \n ", status, error_string);			\
		printf("FUNCTION: %s \n FILE: %s LINE: %d \n", #x, __FILE__, _LINE__);		\
		if (error_string) MEM_free(error_string);									\
		exit(EXIT_FAILURE);															\
	}//if not ITK_OK																\
	}//define ITK_Call																

#define EXIT_FAILURE 1															

	static void doIT{
		int
			prop_count,
			ii,
			n_statuses;

		tag_t
			item_rev,
			* statuses;

		char
			** prop_names;

		ITK_CALL(ITEM_find_rev("block", "A", &item_rev));
		ITK_CALL(AOM_ask_prop_names(item_rev, &prop_count,&prop_names));

		//loop through the properties
		for (ii = 0; ii < prop_count; ii++) {//for
			if (!strcmp(prop_names[ii], "release_statuses")) {//if
				ITK_CALL(AOM_ask_value_tags(item_rev, prop_names[ii], &n_statuses, &statuses));
				printf("There are: %d status tags for this item revision\n", n_statuses);
				if (n_statuses) MEM_free(statuses);
			}//if
		}//for
		MEM_free(prop_names);
	}//doIT

		int ITK_main(int argc, char* argv[]) {	//ITK_main

		int
			status = 0;
		char
			* message;

		ITK_initialize_text_services(0);
		status = ITK_auto_login();

		if ((status != ITK_ok)) printf("Login not successful \n");
		else {
			printf("Login successful \n");
			ITK_set_journaling(TRUE);
			do_it();
		}//else
		ITK_exit_module(TRUE);
		return status;
	}//ITK_main


