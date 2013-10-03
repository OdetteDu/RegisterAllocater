//This class will test rename
loadI 1024 => r1
loadI 1028 => r1
loadI 1032 => r1
loadI 1036 => r1
loadI 1040 => r1
loadI 1044 => r1
loadI 1048 => r1
loadI 1052 => r1
loadI 1056 => r1
loadI 1060 => r1
loadI 1064 => r2
add r1, r2 => r1
mult r1, r2 => r2
loadI 1068 => r3
store r1 => r3
loadI 1072 => r4
store r2 => r4
output 1068
output 1072 

