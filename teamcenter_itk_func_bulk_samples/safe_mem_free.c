/*HEAD SAFE_MEM_FREE CCC ITK */
#define SAFE_MEM_FREE( a )   \
do                          \
{                           \
    if ( (a) != NULL )      \
    {                       \
       MEM_free( (a) );    \
        (a) = NULL;         \
    }                       \
}                           \
while ( 0 )
