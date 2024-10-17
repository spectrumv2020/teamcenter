/*HEAD SERVER_EXIT_CLIENT_INPUT_SELECTED_OBJECTS CCC ITK */
/*
    Client input is selected objects argument.
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
        
            int n_input_args = 1; 
            Object [] input_args = new Object[n_input_args];
            input_args[0] = selected_objects;       
            userService.call("client_input_selected_objects", input_args);
        }
        catch( TCException ex )
        {
            MessageBox.post(ex);
        }

*/

extern DLLAPI int register_client_input_selected_objects_method()
{
    char function_name[] = "client_input_selected_objects";
    USER_function_t function_ptr = client_input_selected_objects;

    /* input arguments passed from client to server */
    int n_input_args = 1;
    int input_args[1] = {USERARG_TAG_TYPE + USERARG_ARRAY_TYPE };

    /* server is not returning data */
    int return_data = USERARG_VOID_TYPE;

    int ifail = ITK_ok;
    ifail = USERSERVICE_register_method(function_name, function_ptr, 
        n_input_args, input_args, return_data);

    return ifail;
}

int client_input_selected_objects(void *return_data)
{
    int ifail = ITK_ok;
    int n_objects = 0;
    tag_t *objects = NULL;
    ifail = USERARG_get_tag_array_argument(&n_objects, &objects);
    for (int ii = 0; ii < n_objects; ii++) 
    {
        ECHO("\t    object tag[%d]: %u \n", ii, objects[ii]);
    }   

    return ifail;
}
