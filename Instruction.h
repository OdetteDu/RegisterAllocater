#ifndef INSTRUCTION_H
#define INSTRUCTION_H

#include <string>
using namespace std;

class Instruction
{
	string opcode;
	int source1,source2,target;
	long immediateValue;

public:
	Instruction(string, long);
	Instruction(string, int, int);
	Instruction(string, int, long);
	Instruction(string, int, int, int);
	~Instruction();

	int getSource1();
	int getSource2();
	int getTarget();
	void print();

	static Instruction* parseLine(string line);
	static int getRegisterNumber(string s);
};



#endif