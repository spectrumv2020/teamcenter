void ask_object_class_name(tag_t object, char **class_name)
{
    tag_t
        class = NULLTAG;

    IFERR_REPORT(POM_class_of_instance(object, &class));
    IFERR_REPORT(POM_name_of_class(class, class_name)); 
}

