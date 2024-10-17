/* HEAD REPORT_ITEM_DESCRIPTION ITK */
static void report_item_description(tag_t object)
{
    WSO_description_t
        desc;
    ITK_CALL(WSOM_describe(object, &desc));
    printf("\n Object Name:     %s", desc.object_name);
    printf("\n   Object Type:     %s", desc.object_type);
    printf("\n   Owner's Name:    %s", desc.owners_name);
    printf("\n   Owning Group:    %s", desc.owning_group_name);
    printf("\n   Description:     %s", desc.description);
    printf("\n   Date Created:    %s", desc.date_created);
    printf("\n   Date Modified:   %s", desc.date_modified);
    printf("\n   Application:     %s", desc.application);
    printf("\n   Date Released:   %s", desc.date_released);
    printf("\n   Released For:    %s", desc.released_for);
    printf("\n   ID String:       %s", desc.id_string);
    printf("\n   Revision Number: %d", desc.revision_number);
    printf("\n   Revision Limit:  %d", desc.revision_limit);
    printf("\n   Last Modifier    %s", desc.last_modifying_user_name);
    printf("\n   Archive Date:    %s", desc.archive_date);
    printf("\n   Backup Date:     %s", desc.backup_date);
    printf("\n   Is Frozen:       %d", desc.is_frozen);
    printf("\n   Is Reserved:     %d", desc.is_reserved);
    printf("\n   Revision ID:     %s", desc.revision_id);   
    printf("\n   Owning Site:     %s\n", desc.owning_site_name);
}
