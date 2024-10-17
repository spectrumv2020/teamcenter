void report_object_type_name(tag_t object)
{
    tag_t
        type = NULLTAG;
    char 
        type_name[TCTYPE_name_size_c+1] = "";

    ERROR_CHECK(TCTYPE_ask_object_type(object, &type));
    ERROR_CHECK(TCTYPE_ask_name(type, type_name)); 
    printf("type_name: %s\n", type_name);   
}
