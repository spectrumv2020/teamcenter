
#include <stdio.h>
#include <qry/qry.h>
#include <tccore/workspaceobject.h>

static void demo_no_entry_query()
{
    tag_t query_tag = NULLTAG;
    IFERR_ABORT(QRY_find("-ReleasedItemRevisions", &query_tag));

    int n_revisions = 0;
    tag_t *revisions = NULL;
    IFERR_ABORT(QRY_execute(query_tag, 0, NULL, NULL, &n_revisions, &revisions));
    printf("\n n_revisions: %d \n", n_revisions);
    for(int ii = 0; ii < n_revisions; ii++)
    {
        char *object_id = NULL;
        IFERR_ABORT(WSOM_ask_object_id_string(revisions[ii], &object_id));
        printf("   %s \n", object_id);
        if (object_id) MEM_free(object_id);
    }
    if (revisions) MEM_free(revisions);
}
