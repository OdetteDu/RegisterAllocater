#include <iostream>
#include <string>
#include <fstream>

using namespace std;

void readFile(string path)
{
	string line;
	ifstream myfile (path);
	if (myfile.is_open())
	{
		while ( getline (myfile,line) )
		{
			cout << line << endl;
		}
		myfile.close();
	}

	else cout << "Unable to open file"; 
}

int main (int argc, const char* argv[])
{
	string mode=argv[1];

	if(mode.compare("t")==0)
	{
		cout<<"Use Top-Down Mode\n";
	}
	else if(mode.compare("b")==0)
	{
		cout<<"Use Buttom-Up Mode\n";
	}
	else
	{
		cout<<"InvalidArgumentException: the argument entered is not valid, should be either b for buttom up, or t for top down.\n";
	}

	int numRegisters=stoi(argv[2]);
	cout<<"Use "<<numRegisters<<" registers.\n";

	string path=argv[3];
	cout<<path<<"\n";
	readFile(path);

	while(1)
	{
	}
	return 0;
}

