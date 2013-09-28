#include "Instruction.h"
#include <iostream>
#include <string>
#include <fstream>
#include <vector>

using namespace std;

vector<Instruction> readFile(string path)
{
	vector<Instruction> instructions;
	//instructions.reserve(2);
	string line;
	ifstream myfile (path);
	if (myfile.is_open())
	{
		while ( getline (myfile,line) )
		{
			Instruction *p=Instruction::parseLine(line);
			if(p!=NULL)
			{
				instructions.push_back(*p);
				//(*p).print();
			}
		}
		myfile.close();
	}

	else cout << "Unable to open file"; 

	return instructions;
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
	vector<Instruction> instructions=readFile(path);
	for(int i=0;i<instructions.size();i++)
	{
		instructions.at(i).print();
	}

	while(1)
	{
	}
	return 0;
}

