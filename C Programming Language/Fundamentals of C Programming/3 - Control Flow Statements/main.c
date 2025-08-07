#include <stdio.h>

int main()
{
	int i = -1;
	
	for(++i;i++;i++) // as i becomes 0, hence the boolean condition check will terminate the loop
		printf("Ashwani is Wrong!\n");
	
	return 0;
}