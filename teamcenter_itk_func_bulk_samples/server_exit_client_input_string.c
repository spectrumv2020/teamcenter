/*HEAD SERVER_EXIT_CLIENT_INPUT_STRING CCC ITK */
/*
    Client input is a string argument.
    Server does not return data.

    Client side code:
       try
       {            
            AbstractAIFApplication app = null;
            app = AIFDesktop.getActiveDesktop().getCurrentApplication();
            TCSession session = (TCSession) app.getSession();
            TCUserService userService = session.getUserService();

            String client_string = new String("Hello Server!");
            int n_input_args = 1;           
            Object [] input_args = new Object[n_input_args];
            input_args[0] = client_string;
            userService.call("client_input_string", input_args);  
        }
        catch( TCException ex )
        {
            MessageBox.post(ex);
        }

*/

extern DLLAPI int register_client_input_string_method()
{
    char function_name[] = "client_input_string";
    USER_function_t function_ptr = client_input_string;

    /* input arguments passed from client to server */
    int n_input_args = 1;
    int input_args[1] = {USERARG_STRING_TYPE};

    /* server is not returning data */
    int return_data = USERARG_VOID_TYPE;

    int ifail = ITK_ok;
    ifail = USERSERVICE_register_method(function_name, function_ptr, 
        n_input_args, input_args, return_data);

    return ifail;
}

int client_input_string(void *return_data)
{
    int ifail = ITK_ok;
    char *client_string = NULL;
    IFERR_REPORT(ifail = USERARG_get_string_argument(&client_string));
    ECHO("\n\t     %s\n", client_string);
    GTAC_free(client_string);

    return ifail;
}
