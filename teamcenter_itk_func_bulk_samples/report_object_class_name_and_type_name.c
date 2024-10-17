void report_object_class_name_and_type_name(tag_t object)
{
    tag_t
        class_tag = NULLTAG,
        type = NULLTAG;
    char 
        *class_name = NULL,
        type_name[TCTYPE_name_size_c+1] = "";

    IFERR_REPORT(POM_class_of_instance(object, &class_tag));
    IFERR_REPORT(POM_name_of_class(class_tag, &class_name)); 
    printf("class_name: %s\n", class_name);
    if (class_name) MEM_free(class_name);

    IFERR_REPORT(TCTYPE_ask_object_type(object, &type));
    IFERR_REPORT(TCTYPE_ask_name(type, type_name)); 
    printf("type_name: %s\n", type_name);   
}
