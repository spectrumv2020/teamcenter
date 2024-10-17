/*HEAD GET_ALL_ICOS_OF_WORKPACEOBJECT.CXX CCC ITK */

static void get_all_icos_of_wso(tag_t wso, int *count, tag_t **icos)
{
    IFERR_REPORT(ICS_ico_find("", wso, 1, count,  icos ));
}