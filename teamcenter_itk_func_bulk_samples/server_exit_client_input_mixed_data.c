/*HEAD SERVER_EXIT_CLIENT_INPUT_MIXED_DATA CCC ITK */
/*
    Client input is object array, string array and a stringargument.
    Server returns no data.

    Client side code:
       try
       {            
            AbstractAIFApplication app = null;
            app = AIFDesktop.getActiveDesktop().getCurrentApplication();
            TCSession session = (TCSession) app.getSession();
            TCUserService userService = session.getUserService();

            InterfaceAIFComponent[] selected_objects = 
                SelectionHelper.getTargetComponents( 
                HandlerUtil.getCurrentSelection(event));
        
            String client_string = new String("Hello Server!");

            String[] client_string_array = 
                new String[]{"string_a", "string_b", "string_c"};

        	int n_input_args = 3; 
        	Object [] input_args = new Object[n_input_args];
        	input_args[0] = selected_objects;   
        	input_args[1] = client_string_array;  
        	input_args[2] = client_string;
        	userService.call("client_input_mixed_data", input_args) ;
        }
        catch( TCException ex )
        {
            MessageBox.post(ex);
        }

*/

extern DLLAPI int register_client_input_mixed_data_method()
{
    char function_name[] = "client_input_mixed_data";
    USER_function_t function_ptr = client_input_mixed_data;

    /* input arguments passed from client to server */
    int n_input_args = 3;
    int input_args[3] = {0, 0, 0};
    input_args[0] = USERARG_TAG_TYPE + USERARG_ARRAY_TYPE;
    input_args[1] = USERARG_STRING_TYPE + USERARG_ARRAY_TYPE;
    input_args[2] = USERARG_STRING_TYPE;

    /* server is not returning data */
    int return_data = USERARG_VOID_TYPE;

    int ifail = ITK_ok;
    ifail = USERSERVICE_register_method(function_name, function_ptr, 
        n_input_args, input_args, return_data);

    return ifail;
}

int client_input_mixed_data(void *return_data)
{
    int ifail = ITK_ok;

    /* Calls to USERARG_get_xxxx fuctions are order dependent */
    int n_objects = 0;
    tag_t *objects = NULL;
    ifail = USERARG_get_tag_array_argument(&n_objects, &objects);
    if (ifail == ITK_ok)
    { 
        for (int ii = 0; ii < n_objects; ii++) 
        {
            TC_write_syslog("object tag[%d]: %u \n", ii, objects[ii]);
        }   

        int n_elements_in_array = 0;
        char **client_string_array = NULL;
        ifail = USERARG_get_string_array_argument(&n_elements_in_array, 
            &client_string_array);

        if (ifail == ITK_ok)
        {
            for (int ii = 0; ii < n_elements_in_array; ii++) 
            {
                TC_write_syslog(" client_string[%d]: %s \n", ii, client_string_array[ii]);
            }
            MEM_free(client_string_array); 

            char *client_string = NULL;
            ifail = USERARG_get_string_argument(&client_string);

            TC_write_syslog("\n\t    %s\n", client_string);
            MEM_free(client_string);
        }
    }
    return ifail;
}
