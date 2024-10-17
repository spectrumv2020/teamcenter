
#include <ae/dataset.h>
#include <sa/am.h>
static int grant_access(tag_t dataset_tag)
{
    int     ifail = ITK_ok;
    tag_t   world_tag = NULLTAG;
    tag_t   write_prev = NULLTAG;

    ifail = AOM_refresh(dataset_tag, POM_modify_lock);
    if (ifail != ITK_ok)
    {
      return ifail;
    }

    ifail = AM_find_named_tag("World", &world_tag);
    if(ifail != ITK_ok)
    {
       AOM_unlock(dataset_tag);
       return ifail;
    }
    ifail = AM_find_privilege("WRITE", &write_prev);
    if(ifail != ITK_ok)
    {
       AOM_unlock(dataset_tag);
       return ifail;
    }
    ifail = AM_grant_privilege(dataset_tag, world_tag , write_prev);
    if(ifail != ITK_ok)
    {
       AOM_unlock(dataset_tag);
       return ifail;
    }
    ifail = AM_save_acl(dataset_tag);
    if(ifail != ITK_ok)
    {
       AOM_unlock(dataset_tag);
       return ifail;
    }

    ifail = AE_save_myself(dataset_tag);
    if(ifail != ITK_ok)
    {
      AOM_unlock(dataset_tag);
      return ifail;
    }
     return ifail;
}
