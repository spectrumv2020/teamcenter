static void report_attachment_information(ITEM_attached_object_tag_t attach)
{
    char
        *id_string,
        *class_name,
        wso_type[WSO_name_size_c +1],
        *relation;
    tag_t
        class;
    
    ITK_CALL(WSOM_ask_object_id_string(attach.attachment, &id_string));
    ITK_CALL(POM_class_of_instance(attach.attachment, &class));
    ITK_CALL(POM_name_of_class(class, &class_name));            
    ITK_CALL(WSOM_ask_object_type(attach.attachment, wso_type));
    ITK_CALL(ITEM_ask_attachment_name(attach.attachment_type, &relation)); 
    printf("\t%s\n\t  Class name: %s\n\t  WSOM Type: %s\n\t  Relation: %s\n", 
        id_string, class_name, wso_type, relation);
    MEM_free(id_string);
    MEM_free(class_name);
    MEM_free(relation);
}
