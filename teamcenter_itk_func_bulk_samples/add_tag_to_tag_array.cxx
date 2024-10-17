
static void add_tag_to_tag_array(tag_t add_tag, int *n_tag_array, tag_ **tag_array)
{
    int count = *n_int_array;
    count++;
    if (count == 1)
    {
     (*tag_array) = (tag_t *) MEM_alloc(sizeof(tag_t));
    }
    else
    {
     (*tag_array) = (tag_t *) MEM_realloc((*tag_array), count * sizeof(tag_t));
    }
    (*tag_array)[count - 1] = add_tag;
    *n_tag_array = count;
}