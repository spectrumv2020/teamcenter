/*HEAD SERVER_EXIT_RETURNS_OBJECT CCC ITK */
/*
    Client input is a blank string argument.
    Server return data is an object.

    Client side code:
       try
       {            
            AbstractAIFApplication app = null;
            app = AIFDesktop.getActiveDesktop().getCurrentApplication();
            TCSession session = (TCSession) app.getSession();
            TCUserService userService = session.getUserService();

            Object[] input_args = new Object[] { "" };
            String itk_function = "server_returns_object";
            TCComponent return_data = 
                (TCComponent) userService.call(itk_function, input_args)        
        }
        catch( TCException ex )
        {
            MessageBox.post(ex);
        }

*/

extern DLLAPI int register_server_returns_object_method()
{
    char function_name[] = "server_returns_object";
    USER_function_t function_ptr = server_returns_object;

    /* input arguments passed from client to server */
    int n_input_args = 1;
    int input_args[1] = {USERARG_STRING_TYPE};

    /* server is returning one object*/
    int return_data = USERARG_TAG_TYPE;

    int ifail = ITK_ok;
    ifail = USERSERVICE_register_method(function_name, function_ptr, 
        n_input_args, input_args, return_data);

    return ifail;
}

int server_returns_object(void *return_data)
{
    int ifail = ITK_ok;

    tag_t new_folder = NULLTAG;
    IFERR_REPORT(ifail = FL_create("New Folder" , "", &new_folder));
    IFERR_REPORT(AOM_save(new_folder));

    if (ifail == ITK_ok)
      *((tag_t *) return_data) = new_folder;
   else
      *((tag_t *) return_data) = NULLTAG;

   return ifail;
}
