/*HEAD SERVER_EXIT_RETURNS_STRING CCC ITK */
/*
    Client input is a blank string argument.
    Server return data is a string.

    Client side code:
       try
       {            
            AbstractAIFApplication app = null;
            app = AIFDesktop.getActiveDesktop().getCurrentApplication();
            TCSession session = (TCSession) app.getSession();
            TCUserService userService = session.getUserService();

           Object[] input_args = new Object[] { "" };
           String itk_function = "server_returns_string";
           String return_data = 
               (String) userService.call(itk_function, input_args);     
        }
        catch( TCException ex )
        {
            MessageBox.post(ex);
        }

*/

extern DLLAPI int register_server_returns_string_method()
{
    char function_name[] = "server_returns_string";
    USER_function_t function_ptr = server_returns_string;

    /* input arguments passed from client to server */
    int n_input_args = 1;
    int input_args[1] = {USERARG_STRING_TYPE};

    /* server is returning a string */
    int return_data = USERARG_STRING_TYPE;

    int ifail = ITK_ok;
    ifail = USERSERVICE_register_method(function_name, function_ptr, 
        n_input_args, input_args, return_data);

    return ifail;
}

int server_returns_string(void *return_data)
{
    char server_string[] = "Hello Client!";

   *((char **) return_data) = 
       (char *) MEM_alloc( (strlen(server_string ) + 1) * sizeof(char));

    strcpy( *((char **) return_data), server_string);

    return ITK_ok;
}
