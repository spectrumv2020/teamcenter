static int pick_one_of_two_options(char *prompt, char *opt1, char *opt2)
{
    char which[10];

    printf("1.  %s\n", opt1);
    printf("2.  %s\n", opt2);
    printf(prompt);
    gets(which);

    return atoi(which);
}

