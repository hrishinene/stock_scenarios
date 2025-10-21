import sys
import argparse

from model.geometry.point import Point
from model.geometry.vector import Vector
from model.link_stack import LinkStack
from model.link import Link
from model.pivot import Pivot
from model.placed_link import PlacedLink
from model.placement import Placement
from process.simple_assembler import SimpleAssembler
from process.solver import Solver
from smart_scripts.input import read_int_array,read_int

def read_placed_link(prompt):
        print(f"read_placed_link ! {prompt}")
        link_length = read_int("Link Length")
        placement_arr = read_int_array("Placement [x,y,z]")
        orientation_arr = read_int_array("Pivot Orientation [x,y,z]")
        
        placed_link = PlacedLink(Link(link_length), Pivot(Placement(Point(placement_arr[0], placement_arr[1], placement_arr[2]), Vector(orientation_arr[0], orientation_arr[1], orientation_arr[2]))))
        print(f"Link Start: {placed_link.get_start()}")
        print(f"Link End: {placed_link.get_end()}")
        
        return placed_link
#===============================================================================
# Smart Functions section
#===============================================================================

def bounding_box_placed_links():
        print("bounding_box_placed_links function called.")
        placed_link = read_placed_link("First")
        bbox = placed_link.get_bounding_box()
        
        print("Bounding Box:")
        bbox.print()
        
def bounding_box_assembly():
        print("bounding_box_assembly function called.")
        
def check_collision_input():
        print("check_collision function called.")
        link1 = read_placed_link("First")
        link2 = read_placed_link("Second")
        
        if(link1.is_colliding(link2)):
            print("Links are colliding.")
        else:
            print("Links are NOT colliding.")
        
def check_collision():
        print("check_collision function called.")
        link1 = PlacedLink(Link(3), Pivot(Placement(Point(0, 0, 0), Vector(0, 0, 1))))
        print(f"Link1 : {link1.get_points()}")
        link2 = PlacedLink(Link(3), Pivot(Placement(Point(1, -1, 0), Vector(1, 0, 0))))
        print(f"Link2 : {link2.get_points()}")
        
        if(link1.is_colliding(link2)):
            print("Links are colliding.")
        else:
            print("Links are NOT colliding.")
        
        
        
def check_validity():
        print("check_validity function called.")

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
                        'boundingBoxLinks': bounding_box_placed_links,
                        'boundingBoxAssembly': bounding_box_assembly,
                        'checkCollision': check_collision,
                        'checkValidity': check_validity,
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