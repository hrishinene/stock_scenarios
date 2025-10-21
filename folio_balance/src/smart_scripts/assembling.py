import sys
import argparse

from model.link_stack import LinkStack
from model.link import Link
from process.simple_assembler import SimpleAssembler
from process.solver import Solver
from smart_scripts.input import read_int_array

def prepare_link_stack(lengths):
        link_stack = LinkStack()
        for length in lengths:
                link_stack.push_link(Link(length))
        return link_stack

def assemble(link_stack, assembler_class):
        assembler = assembler_class(link_stack)
        assembly = assembler.assemble()
        return assembly

#===============================================================================
# Smart Functions section
#===============================================================================
def default_assembly():
        print(f"default_assembly !")
        assembly = assemble(prepare_link_stack([3, 3, 3, 3, 2, 2, 2, 3, 3, 2, 2, 3, 2, 3, 2, 2, 3]), SimpleAssembler)
        assembly.print_assembly()

def solved_assembly():
        print(f"solved_assembly !")
        assembly = assemble(prepare_link_stack([3, 3, 3, 3, 2, 2, 2, 3, 3, 2, 2, 3, 2, 3, 2, 2, 3]), Solver)
        assembly.print_assembly()

def initialize_puzzle():
        print(f"Arrange Links")
        link_lengths = read_int_array("Link Lengths")
        print(f"Link Lengths: {link_lengths}")

        return prepare_link_stack(link_lengths)        

#===============================================================================
# Following is boilerplate code for SmartScript
#===============================================================================
def printUsage():
        print("\n=====================================\n")
        print("Script generated on Tue Jul 15 11:56:49 2025")
        print("This script does CHANGEME. It has public functions so that they can be invoked from outside.")
        print("Usage sampleSmartScript.py -f <Function Name>")
        print("\n=====================================\n")
#===============================================================================

def main():
        parser = argparse.ArgumentParser(
                description="This script does CHANGEME. It has public functions so that they can be invoked from outside."
        )

        try:
                print(f"Parsing arguments: {sys.argv}")

                parser.add_argument(
                        '-f', '--function', type=str, required=True, help='Name of function'
                )
                args = parser.parse_args()

                func_map = {
                        'initialize': initialize_puzzle,
                        'place': default_assembly,
                        'solve': solved_assembly
                }

                if args.function in func_map:
                        func_map[args.function]()
                else:
                        print(f"Unknown function: {args.function}\nAvailable functions are: {', '.join(func_map.keys())}")

        except Exception as e:
                print(f"Error: {e}")
                printUsage()
                sys.exit(1)

if __name__ == '__main__':
        main()