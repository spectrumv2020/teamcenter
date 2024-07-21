*=============================================================================
                Copyright (c) 2003-2005 UGS Corporation
                   Unpublished - All Rights Reserved
===============================================================================
File description:

    Filename: CreateItem.c
    Module  : main

        This program contains all the functions that are used to create an item.

    USAGE : find_released_item_rev [-u=<user_id> -p=<password> [-g=<group>]]
                                   -start_date=<DD-MMM-YYYY HH:MM:SS>
                                   [-end_date=<DD-MMM-YYYY HH:MM:SS>]
                                   [-obj_type=<Object type>]
                                   [-out_file=<output_filename>]
                                   [-h]
 */

/****************************************************************************/
/*                      I N C L U D E   F I L E S                           */
/****************************************************************************/

#include <sys/types.h>
#include <sys/stat.h>
#include <stdlib.h>

#include <unidefs.h>

#include <base_utils/Mem.h>
#include <fclasses/tc_date.h>
#include <fclasses/tc_string.h>
#include <nls/nls.h>
#include <pom/pom/pom.h>
#include <ps/ps.h>
#include <string.h>
#include <tc/emh.h>
#include <tc/tc_util.h>
#include <tccore/item.h>
#include <tccore/workspaceobject.h>
#include <tcinit/tcinit.h>