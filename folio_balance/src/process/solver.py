from model.assembly import Assembly
from model.geometry.bounding_box import BoundingBox
from model.geometry.point import Point
from model.geometry.vector import Vector
from model.link_stack import LinkStack
from model.pivot import Pivot
from model.placed_link import PlacedLink
from model.placement import Placement
from process.assembler import Assembler


class Solver(Assembler):
    def __init__(self, link_stack: LinkStack):
        self.link_stack = link_stack

    def is_placeable(assembly: Assembly, new_link: PlacedLink) -> bool:
        # Check for collisions or out of bounds
        new_bounding_box:BoundingBox = assembly.get_bounding_box().add(new_link.get_bounding_box())
        if new_bounding_box.get_max_dimension() > 3 or assembly.collides(new_link):
            return False
        return True
   
    def place_link(assembly: Assembly, link_stack: LinkStack): 
        # If all are placed, return True
        if link_stack.size() == 0:
            return True
       
        # Take the next link and try to place it in all possible orientation
        link = link_stack.pop_link()
        last_placed_link:PlacedLink = assembly.placed_links[-1]
        pivot:Pivot = last_placed_link.end_pivot
      
        # Assemble the link at the current pivot orientation 
        placed_link = PlacedLink(link, pivot)
        assembly.place_link(placed_link)
        while True:
            if assembly.is_valid():
                if Solver.place_link(assembly, link_stack):
                    return True
            
            if not placed_link.rotate():
                break
        
        # replace the link and backtrack
        assembly.placed_links.pop()  # Backtrack
        link_stack.push_link(link)  # Backtrack
        return False # No valid placement found
            
    def assemble(self):
        # Simple assembly logic place the links in default positions
        # Place first link and then solve for the rest
        assembly = Assembly()
        pivot = Pivot(Placement(Point(0, 0, 0), Vector(0, 0, 1)))  # Default pivot along z-axis
        link = self.link_stack.pop_link()
        placed_link = PlacedLink(link, pivot)
        assembly.place_link(placed_link)
        
        # Now solve for the remaining links with recursive backtracking
        if Solver.place_link(assembly, self.link_stack):
            return assembly

        return None # No solution found
        
        