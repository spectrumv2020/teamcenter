/*HEAD SERVER_EXIT_CLIENT_INPUT_STRING_ARRAY CCC ITK */
/*
    Client input is a string array.
    Server Server does not return data..

    Client side code:
       try
       {            
            AbstractAIFApplication app = null;
            app = AIFDesktop.getActiveDesktop().getCurrentApplication();
            TCSession session = (TCSession) app.getSession();
            TCUserService userService = session.getUserService();

            String[] client_string_array =
                new String[]{"string_a", "string_b", "string_c"};
            int n_input_args = 1; 
            Object [] input_args = new Object[n_input_args];
            input_args[0] = client_string_array;            
            userService.call("client_input_string_array", input_args) ;
        }
        catch( TCException ex )
        {
            MessageBox.post(ex);
        }

*/

extern DLLAPI int register_client_input_string_array_method()
{
    char function_name[] = "client_input_string_array";
    USER_function_t function_ptr = client_input_string_array;

    /* input arguments passed from client to server */
    int n_input_args = 1;
    int input_args[1] = {USERARG_STRING_TYPE + USERARG_ARRAY_TYPE };

    /* server is not returning data */
    int return_data = USERARG_VOID_TYPE;

    int ifail = ITK_ok;
    ifail = USERSERVICE_register_method(function_name, function_ptr, 
        n_input_args, input_args, return_data);

    return ifail;
}

int client_input_string_array(void *return_data)
{
    int ifail = ITK_ok;
    int n_elements_in_array = 0;
    char **client_string_array = NULL;
    ifail = USERARG_get_string_array_argument(&n_elements_in_array, 
        &client_string_array);
    for (int ii = 0; ii < n_elements_in_array; ii++) 
    {
        ECHO("\t    client_string[%d]: %s \n", ii, client_string_array[ii]);
    }
    GTAC_free(client_string_array);

    return ifail;
}
