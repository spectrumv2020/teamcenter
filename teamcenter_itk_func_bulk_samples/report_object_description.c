static void report_object_description(tag_t object)
{
    WSO_description_t
        desc;

    if (IFERR_REPORT(WSOM_describe(object, &desc))) return;

    ECHO("     Object Name:     %s\n", desc.object_name);
    ECHO("     Object Type:     %s\n", desc.object_type);
    ECHO("     Owner's Name:    %s\n", desc.owners_name);
    ECHO("     Owning Group:    %s\n", desc.owning_group_name);
    ECHO("     Description:     %s\n", desc.description);
    ECHO("     Date Created:    %s\n", desc.date_created);
    ECHO("     Date Modified:   %s\n", desc.date_modified);
    ECHO("     Application:     %s\n", desc.application);
    ECHO("     Date Released:   %s\n", desc.date_released);
    ECHO("     Released For:    %s\n", desc.released_for);
    ECHO("     ID String:       %s\n", desc.id_string);
    ECHO("     Revision Number: %d\n", desc.revision_number);
    ECHO("     Revision Limit:  %d\n", desc.revision_limit);
    ECHO("     Last Modifier    %s\n", desc.last_modifying_user_name);
    ECHO("     Archive Date:    %s\n", desc.archive_date);
    ECHO("     Backup Date:     %s\n", desc.backup_date);
    ECHO("     Is Frozen:       %d\n", desc.is_frozen);
    ECHO("     Is Reserved:     %d\n", desc.is_reserved);
    ECHO("     Revision ID:     %s\n", desc.revision_id);   
    ECHO("     Owning Site:     %s\n", desc.owning_site_name);
}

