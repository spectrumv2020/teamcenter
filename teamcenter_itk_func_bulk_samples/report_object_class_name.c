void report_object_class_name(tag_t object)
{
    tag_t
        class = NULLTAG;
    char 
        *class_name = NULL;

    ERROR_CHECK(POM_class_of_instance(object, &class));
    ERROR_CHECK(POM_name_of_class(class, &class_name)); 
    printf("class_name: %s\n", class_name);
    if (class_name) MEM_free(class_name);
}
