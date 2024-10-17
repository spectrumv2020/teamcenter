
#include <ug_va_copy.h>
#include <A2gtac/A2itemCreatePostExtension.hxx>
#include <metaframework/CreateInput.hxx>

int A2itemCreatePostExtension( METHOD_message_t *msg, va_list args )
{
    printf("\n\n A2itemCreatePostExtension \n\n");

	int ifail = ITK_ok;

    va_list largs;
    va_copy( largs, args );

	Teamcenter::CreateInput *creInput = 
		va_arg(largs, Teamcenter::CreateInput*);

    va_end( largs );

	tag_t new_item = msg->object_tag;
	printf("    new_item: %u \n", new_item);

	bool isNull = true;
	tag_t new_rev = NULLTAG;
	ifail = creInput->getTag("revision", new_rev, isNull);
	if((isNull == FALSE)&& (ifail == ITK_ok))
	{
		printf("    new_rev: %u \n", new_rev);
	}

	tag_t master_form = NULLTAG;
	ifail = creInput->getTag("IMAN_master_form", master_form, isNull);
	if((isNull == FALSE)&& (ifail == ITK_ok))
	{
		printf("    master_form: %s \n", master_form);
	}

	std::string item_id = "";
	ifail = creInput->getString("item_id", item_id, isNull);
	if((isNull == FALSE)&& (ifail == ITK_ok))
	{
		printf("    item_id: %s \n", item_id.c_str());
	}

	std::string name = "";
	ifail = creInput->getString("object_name", name, isNull);
	if((isNull == FALSE)&& (ifail == ITK_ok))
	{
		printf("    object_name: %s \n", name.c_str());
	}

    return ifail;
}
