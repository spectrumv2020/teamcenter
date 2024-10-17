
static void add_int_to_int_array(int add_int, int *n_int_array, int **int_array)
{
    int count = *n_int_array;
    count++;
    if (count == 1)
    {
     (*int_array) = (int *) MEM_alloc(sizeof(int));
    }
    else
    {
     (*int_array) = (int *) MEM_realloc((*int_array), count * sizeof(int));
    }
    (*int_array)[count - 1] = add_int;
    *n_int_array = count;
}