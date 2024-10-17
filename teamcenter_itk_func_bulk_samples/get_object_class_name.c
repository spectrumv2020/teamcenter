void get_object_class_name(tag_t object, char **class_name)
{
    tag_t
        class = NULLTAG;

    ERROR_CHECK(POM_class_of_instance(object, &class));
    ERROR_CHECK(POM_name_of_class(class, class_name)); 
}

