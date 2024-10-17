void get_object_class_name_and_type_name(tag_t object, char **class_name, 
         char type_name[TCTYPE_name_size_c+1])
{
    tag_t
        class = NULLTAG, 
        type = NULLTAG;

    IFERR_REPORT(POM_class_of_instance(object, &class));
    IFERR_REPORT(POM_name_of_class(class, class_name)); 
  
    IFERR_REPORT(TCTYPE_ask_object_type (object, &type));
    IFERR_REPORT(TCTYPE_ask_name (type, type_name)); 
}
