// COMP 412, LAB 1, Block "report3.i"
//
// Fibonacci numbers, using a heap of registers (25)
//
// Example usage: sim < report3.i
	loadI	0 => r0
	loadI	4 => r1
	loadI	0 => r2
	loadI	1 => r3
	loadI   2000 => r20
// f1
	add	r2,r3 => r4
// f2
	add	r4,r2 => r5
// f3
	add	r5,r4 => r6
// f4
	add	r6,r5 => r7
// f5
	add	r7,r6 => r8
// f6
	add	r8,r7 => r9
// f7
	add	r9,r8 => r10
// f8
	add	r10,r9 => r11
// f9
	add	r11,r10 => r12
// f10
	add	r12,r11 => r13
// 
	store	r2 => r20
	add	r20,r1 => r21
	store	r4 => r21
	add	r21,r1 => r22
	store	r5 => r22
	add	r22,r1 => r23
	store	r6 => r23
	add	r23,r1 => r24
	store	r7 => r24
	add	r24,r1 => r25
	store	r8 => r25
	add	r25,r1 => r26
	store	r9 => r26
	add	r26,r1 => r27
	store	r10 => r27
	add	r27,r1 => r28
	store	r11 => r28
	add	r28,r1 => r29
	store	r12 => r29
	add	r29,r1 => r30
	store	r13 => r30
//
	output 	2000
	output 	2004
	output 	2008
	output 	2012
	output 	2016
	output 	2020
	output 	2024
	output 	2028
	output 	2032
	output 	2036
	output 	2040
// end of block
