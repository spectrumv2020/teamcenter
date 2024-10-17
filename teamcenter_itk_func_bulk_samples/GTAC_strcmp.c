static int GTAC_strcmp(const char *s1, const char *s2)
{
     int ret_val = 0;
     char *s1_disp_name = NULL;
     char *s2_disp_name = NULL;

     PROPDESC_ask_display_name_by_name(s1, &s1_disp_name);
     PROPDESC_ask_display_name_by_name(s2, &s2_disp_name);

     ret_val = strcmp(s1_disp_name, s2_disp_name);

     if (s1_disp_name) MEM_free(s1_disp_name);
     if (s2_disp_name) MEM_free(s2_disp_name);

     return ret_val;
}

