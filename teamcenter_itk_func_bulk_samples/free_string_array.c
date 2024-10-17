static void free_string_array(int n_elements, char **string_array)
{
    int ii;

    for (ii = 0; ii < n_elements; ii++)
        if (string_array[ii]) MEM_free(string_array[ii]);

    if ((n_elements > 0) && (string_array != NULL)) MEM_free(string_array);
}

