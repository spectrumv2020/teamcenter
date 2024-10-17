void get_object_type_name(tag_t object, char type_name[TCTYPE_name_size_c+1])
{
    tag_t
        type = NULLTAG;

    ERROR_CHECK(TCTYPE_ask_object_type (object, &type));
    ERROR_CHECK(TCTYPE_ask_name (type, type_name)); 
}
