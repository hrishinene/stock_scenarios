from model.geometry.vector import Vector


class Point:
    def __init__(self, x: int, y: int, z: int):
        self.x = x
        self.y = y
        self.z = z
        
    def offset(self, vector):
        return Point(self.x + vector.x, self.y + vector.y, self.z + vector.z)
    
    def offset_distance(self, vector:Vector, distance):
        unit_vector = vector.get_unit_vector()
        return Point(self.x + unit_vector.x * distance,
                     self.y + unit_vector.y * distance,
                     self.z + unit_vector.z * distance)
    
    def __repr__(self):
        return f"Point({self.x}, {self.y}, {self.z})"