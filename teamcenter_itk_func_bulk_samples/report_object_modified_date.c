static void report_object_modified_date(tag_t object)
{
	date_t
		the_date;
	char
		*last_modified;

	if (IFERR_REPORT(AOM_ask_modified_date(object, &the_date))) return;
	if (IFERR_REPORT(ITK_date_to_string(the_date, &last_modified))) return;
	if (last_modified)
	{
		ECHO((" last modified = %s\n", last_modified));
		MEM_free(last_modified);
	}
}

