
static void add_int_to_int_array(int add_int, int *n_int_array, int **int_array)
{
    int count = *n_int_array;
    count++;
    if (count == 1)
    {
     (*int_array) = (int *) MEM_alloc(sizeof(int));
    }
    else
    {
     (*int_array) = (int *) MEM_realloc((*int_array), count * sizeof(int));
    }
    (*int_array)[count - 1] = add_int;
    *n_int_array = count;
}

static void add_tag_to_tag_array(tag_t add_tag, int *n_tag_array, tag_t **tag_array)
{
    int count = *n_tag_array;
    count++;
    if (count == 1)
    {
     (*tag_array) = (tag_t *) MEM_alloc(sizeof(tag_t));
    }
    else
    {
     (*tag_array) = (tag_t *) MEM_realloc((*tag_array), count * sizeof(tag_t));
    }
    (*tag_array)[count - 1] = add_tag;
    *n_tag_array = count;
}

static void revise_item_revisions(int num_target_objs, tag_t *target_object_tags)
{
    /* Pointer to an array of integers that stores the number of attached 
     *  objects of each target revision.  For example, with the objects
     *  below the array would be {2,4,3}.
     
     *  ItemRevision
     *      ItemRevision Master
     *      UGMASTER
     *
     *  ItemRevision
     *      ItemRevision Master
     *      UGMASTER
     *      UPART
     *      DirectModel
     *      
     *  ItemRevision)
     *      ItemRevision Master
     *      UGMASTER
     *      UPART
    */
    int *all_attached_object_count = NULL; 
    
    /* Pointer to an array of attached object_tag tags of each target revision. 
     * Normally this would be tag_t**.  Although tag_t* is unconventional 
     * it is not incorrect.  Using the same example objects as above 
     * the resulting array would be the tags of these objects
     *
     *      ItemRevision Master
     *      UGMASTER
     *      ItemRevision Master
     *      UGMASTER
     *      UPART
     *      DirectModel
     *      ItemRevision Master
     *      UGMASTER
     *      UPART
    */     
    tag_t *all_deepcopydata_tags;
    
    int n_ints_in_list = 0;
    int n_tags_in_list = 0;
    int *ifails = NULL;       
    char *id_string = NULL;
    char type_name[TCTYPE_name_size_c + 1] = "";
    tag_t object_tag = NULLTAG;
    tag_t type_tag = NULLTAG;     
    tag_t *attached_objs_tags = NULL;
    tag_t *target_copy_tags = NULL;        
    tag_t *revise_input_tags = NULL;
    
    revise_input_tags = (tag_t *) MEM_alloc(num_target_objs * sizeof(tag_t));
    
    for (int ii = 0; ii < num_target_objs; ii++)
    {  
        tag_t type_tag = NULLTAG;
        IFERR_REPORT(TCTYPE_ask_object_type(target_object_tags[ii], &type_tag));
        
        tag_t revise_input_tag = NULLTAG;
        IFERR_REPORT(TCTYPE_construct_operationinput(type_tag, 
            TCTYPE_OPEARTIONINPUT_REVISE, &revise_input_tag)); 
        revise_input_tags[ii] = revise_input_tag; 

        printf("\nTarget Objects::\n");
        int attached_object_count = 0;
        tag_t *deepcopydata_tags = NULL;
        IFERR_REPORT(TCTYPE_ask_deepcopydata(target_object_tags[ii], 
            TCTYPE_OPEARTIONINPUT_REVISE, &attached_object_count, &deepcopydata_tags)); 
        tag_t last_object = NULLTAG;
        for (int jj = 0; jj < attached_object_count; jj++)
        {   
            IFERR_REPORT(AOM_ask_value_tag(deepcopydata_tags[jj], "targetObject", 
                &object_tag)); 
            if(object_tag != last_object)
            {   IFERR_REPORT(WSOM_ask_object_id_string(object_tag,&id_string));              	
                IFERR_REPORT(TCTYPE_ask_object_type(object_tag, &type_tag));                
                IFERR_REPORT(TCTYPE_ask_name(type_tag, type_name));  
                printf("    %s (%s)\n", id_string, type_name);  
            }
            last_object = object_tag;
               
            IFERR_REPORT(AOM_ask_value_tag(deepcopydata_tags[jj], "attachedObject", &object_tag)); 
            if(object_tag != NULLTAG)
            {    
                IFERR_REPORT(WSOM_ask_object_id_string(object_tag,&id_string));              	
                IFERR_REPORT(TCTYPE_ask_object_type(object_tag, &type_tag));                
                IFERR_REPORT(TCTYPE_ask_name(type_tag, type_name)); 
                printf("        attachedObject: %s (%s)\n", id_string, type_name);  
            }
        }

        if (attached_object_count > 0)
        {
            add_int_to_int_array(attached_object_count, &n_ints_in_list, 
                &all_attached_object_count);           
            for(int jj = 0; jj < attached_object_count; jj++)
            {
                add_tag_to_tag_array(deepcopydata_tags[jj], &n_tags_in_list,
                    &all_deepcopydata_tags);
            }
        }       
        if(deepcopydata_tags) MEM_free(deepcopydata_tags);
    }
    
    IFERR_REPORT(TCTYPE_revise_objects(num_target_objs, target_object_tags,
        revise_input_tags, all_attached_object_count, all_deepcopydata_tags, 
        &target_copy_tags,  &ifails));
        
    printf("\nNew Revisions:\n");   
    for(int ii = 0; ii < num_target_objs; ii++)
    {
        
        IFERR_REPORT(WSOM_ask_object_id_string(target_copy_tags[ii],
            &id_string));              	
        IFERR_REPORT(TCTYPE_ask_object_type(target_copy_tags[ii], &type_tag));                
        IFERR_REPORT(TCTYPE_ask_name(type_tag, type_name));  
        
        if(ifails[ii] == ITK_ok) printf("   %s (%s)\n", id_string, type_name);
        else
        {  
            char *error_message_string;
            EMH_get_error_string (NULLTAG,  ifails[ii], &error_message_string);
            printf("\t%d %s\n", ifails[ii], error_message_string);
            if(error_message_string) MEM_free(error_message_string);
        }
    }
            
    if(revise_input_tags) MEM_free(revise_input_tags);
    if(all_attached_object_count) MEM_free(all_attached_object_count);
    if(all_deepcopydata_tags) MEM_free(all_deepcopydata_tags);
    if(attached_objs_tags) MEM_free(attached_objs_tags);  
    if(target_copy_tags) MEM_free(target_copy_tags);
    if(ifails) MEM_free(ifails);
    if(id_string) MEM_free(id_string);
}

static void ECHO(char *format, ...)
{
    char msg[1000];
    va_list args;
    va_start(args, format);
    vsprintf(msg, format, args);
    va_end(args);
    printf(msg);
    TC_write_syslog(msg);
}

static int report_error(char *file, int line, char *call, int status,
    logical exit_on_error)
{
    if (status != ITK_ok)
    {
        int
            n_errors = 0;
        const int
            *severities = NULL,
            *statuses = NULL;
        const char
            **messages;

        EMH_ask_errors(&n_errors, &severities, &statuses, &messages);
        if (n_errors > 0)
        {
            ECHO("\n%s\n", messages[n_errors-1]);
            EMH_clear_errors();
        }
        else
        {
            char *error_message_string;
            EMH_get_error_string (NULLTAG, status, &error_message_string);
            ECHO("\n%s\n", error_message_string);
        }

        ECHO("error %d at line %d in %s\n", status, line, file);
        ECHO("%s\n", call);
        if (exit_on_error)
        {
            ECHO("%s", "Exiting program!\n");
            exit (status);
        }
    }
    return status;
}


