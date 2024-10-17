void POMEnquiry_ItemRevs_By_PID()
{
	int ifail = 0;
	int rows = 0;
	int cols = 0;  
	void ***ppReport = NULL;
	char *pQry1 = "QueryItemRevByPID";
	char *pClass = "POM_object"; // POM_object.pid
	const char *select_attrs[] = { "puid" };
	const int pid_values[] = { 1026, 1069, 1156, 1162, 1200 }; // Some known pid values for ItemRevision.
	int int_value = 0;

	POM_enquiry_create(pQry1);
	POM_enquiry_add_select_attrs(pQry1, pClass, 1, select_attrs);
	POM_enquiry_set_int_value(pQry1, "intValueId", 1, &int_value, POM_enquiry_bind_value); 
	POM_enquiry_set_attr_expr(pQry1, "pidExp", pClass, "pid", POM_enquiry_equal, "intValueId");
	POM_enquiry_set_where_expr(pQry1, "pidExp");

	for(int i = 0; i < 5; i++)
	{
		int_value = pid_values[i];
		POM_enquiry_set_int_value(pQry1, "intValueId", 1, &int_value, POM_enquiry_bind_value); 
		POM_enquiry_execute(pQry1, &rows, &cols, &ppReport);
		printf("PID: %d, Total ItemRevision tags: %d\n", int_value, rows);

		for(int ii = 0; ii < rows; ii++)
		{
			tag_t tag = *(tag_t*)ppReport[ii][0];
			printf("%u, ", tag);
		}

		printf("\n");
		MEM_free(ppReport);
	}

	POM_enquiry_delete(pQry1);
}