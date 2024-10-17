/*HEAD SERVER_EXIT_RETURNS_STRING_ARRAY CCC ITK */
/*
    Client input is a blank string argument.
    Server return data is a string array.

    Client side code:
       try
       {            
            AbstractAIFApplication app = null;
            app = AIFDesktop.getActiveDesktop().getCurrentApplication();
            TCSession session = (TCSession) app.getSession();
            TCUserService userService = session.getUserService();

            Object[] input_args = new Object[] { "" };
            String itk_function = "server_returns_string_array";
            String return_data[] = 
               (String[]) userService.call(itk_function, input_args);       
        }
        catch( TCException ex )
        {
            MessageBox.post(ex);
        }

*/

extern DLLAPI int register_server_returns_string_array_method()
{
    char function_name[] = "server_returns_string_array";
    USER_function_t function_ptr = server_returns_string_array;

    /* input arguments passed from client to server */
    int n_input_args = 1;
    int input_args[1] = {USERARG_STRING_TYPE};

    /* server is returning string array*/
    int return_data = USERARG_STRING_TYPE + USERARG_ARRAY_TYPE;

    int ifail = ITK_ok;
    ifail = USERSERVICE_register_method(function_name, function_ptr, 
        n_input_args, input_args, return_data);

    return ifail;
}

int server_returns_string_array(void *return_data)
{
    char server_strings[3] [16] = {"string_a", "string_b", "string_c"}; 

    int array_size = 3; 
    char **string_array = NULL;
    string_array =  (char **) MEM_alloc(array_size * sizeof(char*));

    string_array[0] = server_strings[0];
    string_array[1] = server_strings[1];
    string_array[2] = server_strings[2];

    int ifail = ITK_ok;
    USERSERVICE_array_t array_struct;
    ifail = USERSERVICE_return_string_array(
        (const char**)string_array, array_size, &array_struct);

    if (array_struct.length != 0)
        *((USERSERVICE_array_t*) return_data) = array_struct;

    GTAC_free(string_array);

    return ifail;
}
