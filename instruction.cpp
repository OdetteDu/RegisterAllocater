#include "Instruction.h"
#include <sstream>
#include <vector>
#include <iostream>
#include <iterator>

Instruction::Instruction(string opcode,long immediateValue)
{
	this->opcode=opcode;
	this->immediateValue=immediateValue;
	this->source1=-1;
	this->source2=-1;
	this->target=-1;
}

Instruction::Instruction(string opcode,int source1, int target)
{
	this->opcode=opcode;
	this->source1=source1;
	this->target=target;
	this->source2=-1;
	this->immediateValue=-1;
}

Instruction::Instruction(string opcode, int target,long immediateValue)
{
	this->opcode=opcode;
	this->target=target;
	this->immediateValue=immediateValue;
	this->source1=-1;
	this->source2=-1;
}

Instruction::Instruction(string opcode,int source1,int source2, int target)
{
	this->opcode=opcode;
	this->source1=source1;
	this->source2=source2;
	this->target=target;
	this->immediateValue=-1;
}

Instruction::~Instruction()
{

}

int Instruction::getSource1()
{
	return source1;
}

int Instruction::getSource2()
{
	return source2;
}

int Instruction::getTarget()
{
	return target;
}

void Instruction::print()
{
	cout<<"Opcode="<<opcode<<" Source1="<<source1<<" Source2="<<source2
		<<" ImmediateValue="<<immediateValue<<" Target="<<target<<"\n";
}

Instruction* Instruction::parseLine(string line)
{
	vector<string> tokens;
	istringstream iss(line);
	copy(istream_iterator<string>(iss),
		istream_iterator<string>(),
		back_inserter<vector<string>>(tokens));

	if(!tokens.empty())
	{
		string temp=tokens.at(0);

		if(temp.compare("//")==0)
		{
			cout<<"This line is comment\n";
			return NULL;//return existing instructions
		}
		else if((temp.compare("load")==0) || (temp.compare("store")==0))
		{
			int source1=getRegisterNumber(tokens.at(1));
			int target=getRegisterNumber(tokens.at(3));
			Instruction *p=new Instruction (temp,source1,target);
			return p;
		}
		else if((temp.compare("add")==0)||(temp.compare("sub")==0)||(temp.compare("mult")==0)||(temp.compare("lshift")==0)||(temp.compare("rshift")==0))
		{
			if(tokens.size()==5)
			{
				int source1=getRegisterNumber(tokens.at(1));
				int source2=getRegisterNumber(tokens.at(2));
				int target=getRegisterNumber(tokens.at(4));
				Instruction *p=new Instruction (temp,source1,source2,target);
				return p;
			}
			else if(tokens.size()==4)
			{
				temp=tokens.at(1);
				int pos=temp.find_first_of(",");
				string temp1=temp.substr(0,pos);
				int source1=getRegisterNumber(temp1);
				string temp2=temp.substr(pos+1,temp.length()-(pos+1));
				int source2=getRegisterNumber(temp2);
				int target=getRegisterNumber(tokens.at(3));
				Instruction *p=new Instruction (tokens.at(0),source1,source2,target);
				return p;
			}
			else
			{
				cout<<"Invalid line\n";
			}
		}
		else if((temp.compare("loadI")==0))
		{
			int target=getRegisterNumber(tokens.at(3));
			long immediate=stoi(tokens.at(1));
			Instruction *p=new Instruction (temp,target,immediate);
			return p;
		}
		else if((temp.compare("output")==0))
		{
			long immediate=stoi(tokens.at(1));
			Instruction *p=new Instruction (temp,immediate);
			return p;
		}
		else
		{
			cout<<"Invalid line\n";
			return NULL;
		}

	}
	else
	{
		cout<<"This line is empty\n";
		return NULL;
	}
}

int Instruction::getRegisterNumber(string s)
{
	if(s.substr(0,1).compare("r")==0)
	{
		if(s.substr(s.length()-1,1).compare(",")==0)
		{
			s=s.substr(1,s.length()-2);
		}
		else
		{
			s=s.substr(1,s.length()-1);
		}

		return stoi(s);
	}
	else
	{
		cout<<"Invalid register name\n";
	}
	return -1;
}

