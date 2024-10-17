
#include <bom/bom.h>
#include <cfm/cfm.h>	
#include <me/me.h>
#include <ps/ps.h>
#include <tccore/aom.h>
#include <tccore/item.h>

static void create_bop_window(tag_t tProcess, tag_t tProcessRevision, tag_t tOperation, tag_t tOperationRevision)
{
	IFERR_REPORT(AOM_refresh(tProcess, TRUE));

	tag_t tWindow = NULLTAG;
	IFERR_REPORT(ME_create_bop_window(&tWindow));

	tag_t tRule = NULLTAG; 
	IFERR_REPORT(CFM_find("Latest Working", &tRule));    
	IFERR_REPORT(BOM_set_window_config_rule(tWindow, tRule));

	tag_t tTopLine = NULLTAG;
	IFERR_REPORT(BOM_set_window_top_line(tWindow, tProcess, NULLTAG, NULLTAG, &tTopLine));	
	
	int iNumBVs = 0;
    tag_t *ptBVs = NULL;
    IFERR_REPORT(ITEM_list_bom_views(tProcess, &iNumBVs, &ptBVs));
	if (iNumBVs == 0)
	{  
		tag_t tBv = NULLTAG;
		IFERR_REPORT(PS_create_bom_view (NULLTAG, "", "", tProcess, &tBv));
		IFERR_REPORT(AOM_save(tBv));

		tag_t tBvr = NULLTAG;
		IFERR_REPORT(PS_create_bvr (tBv, "", "",  false, tProcessRevision, &tBvr));
		IFERR_REPORT(AOM_save (tBvr));
		IFERR_REPORT(AOM_save (tProcess));
	}
	if(ptBVs) MEM_free(ptBVs);
	tag_t tChildLine = NULLTAG;
	IFERR_REPORT(BOM_line_add(tTopLine, tOperation, tOperationRevision, NULLTAG, &tChildLine));

	IFERR_REPORT(BOM_save_window(tWindow)); 
	IFERR_REPORT(BOM_close_window(tWindow));

	IFERR_REPORT(AOM_refresh(tProcess, FALSE));
	IFERR_REPORT(AOM_unload(tProcess));
}