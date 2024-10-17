/*HEAD SERVER_EXIT_RETURNS_OBJECT_ARRAY CCC ITK */
/*
    Client input is a blank string argument.
    Server return data is an object array.

    Client side code:
       try
       {            
            AbstractAIFApplication app = null;
            app = AIFDesktop.getActiveDesktop().getCurrentApplication();
            TCSession session = (TCSession) app.getSession();
            TCUserService userService = session.getUserService();

            Object[] input_args = new Object[] { "" };
            String itk_function = "server_returns_object_array";
            TCComponent[] return_data = 
                (TCComponent[]) userService.call(itk_function, input_args);         
        }
        catch( TCException ex )
        {
            MessageBox.post(ex);
        }

*/

extern DLLAPI int register_server_returns_object_array_method()
{
    char function_name[] = "server_returns_object_array";
    USER_function_t function_ptr = server_returns_object_array;

    /* input arguments passed from client to server */
    int n_input_args = 1; 
    int input_args[1] = {USERARG_STRING_TYPE};

    /* server is returning an array of objects (a.k.a. tags) */
    int return_data = USERARG_TAG_TYPE + USERARG_ARRAY_TYPE;

    int ifail = ITK_ok;
    ifail = USERSERVICE_register_method(function_name, function_ptr, 
        n_input_args, input_args, return_data);

    return ifail;
}

int server_returns_object_array(void *return_data)
{
    int ifail = ITK_ok;

    int n_objs = 0;
    tag_t *obj_array = NULL;
    IFERR_REPORT(TCTYPE_find_types_for_class ("Item", &n_objs, &obj_array));

    ifail = USERSERVICE_return_tag_array(obj_array, n_objs, 
        (USERSERVICE_array_t *) return_data);   
    
    GTAC_free(obj_array);
    return ifail;
}


