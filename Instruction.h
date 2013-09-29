#ifndef INSTRUCTION_H
#define INSTRUCTION_H

#include <string>
using namespace std;

struct Register
{
	int pr;
	int vr;
	int lastUse;
	int define;
	int *count;
};

class Instruction
{
	string opcode;
	Register source1,source2,target;
	long immediateValue;

public:
	void init();
	void initRegister(Register &r);
	Instruction(string, long);
	Instruction(string, int, int);
	Instruction(string, int, long);
	Instruction(string, int, int, int);
	~Instruction();
	void deleteRegister(Register &r);

	Register getSource1();
	Register getSource2();
	Register getTarget();

	void print();

	static Instruction* parseLine(string line);
	static int getRegisterNumber(string s);
};





#endif