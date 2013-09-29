#include "Instruction.h"
#include <iostream>
#include <string>
#include <fstream>
#include <vector>
#include <map>

using namespace std;

vector<Instruction*> readFile(string path)
{
	vector<Instruction*> instructions;
	//instructions.reserve(10);
	string line;
	ifstream myfile (path);
	if (myfile.is_open())
	{
		while ( getline (myfile,line) )
		{
			Instruction *p=Instruction::parseLine(line);
			if(p!=NULL)
			{
				instructions.push_back(p);
				//(*p).print();
			}
		}
		myfile.close();
	}

	else cout << "Unable to open file"; 

	return instructions;
}

void calculateLiveRange(vector<Instruction*> instructions)
{
	int i;
	map<int,int> useList;
	for(i=instructions.size()-1;i>=0;i--)
	{
		Instruction temp=(*instructions.at(i));
		if(temp.getSource1().vr!=-1)
		{
			useList.insert(pair<int,int>(temp.getSource1().vr,i));
		}

		if(temp.getSource2().vr!=-1)
		{
			useList.insert(pair<int,int>(temp.getSource2().vr,i));
		}

		if(temp.getTarget().vr!=-1)
		{
			int define=temp.getTarget().vr;
			map<int,int>::iterator iter;
			iter=useList.find(define);
			if(iter!=useList.end())
			{
				int use=iter->second;
				Register r=(*instructions.at(i)).getTarget();
				r.lastUse=use;
				r.define=i;
			}
		}
	}
	//print
	for(i=0;i<instructions.size();i++)
	{
		(*instructions.at(i)).print();
	}
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
	vector<Instruction*> instructions=readFile(path);
	calculateLiveRange(instructions);

	while(1)
	{
	}
	return 0;
}

