import NavbarItem from 'app/layouts/navbar/navbar-item.model';

export const EntityNavbarItems: NavbarItem[] = [
  {
    name: 'Author',
    route: '/author',
    translationKey: 'global.menu.entities.author',
  },
  {
    name: 'Post',
    route: '/post',
    translationKey: 'global.menu.entities.post',
  },
  {
    name: 'Comment',
    route: '/comment',
    translationKey: 'global.menu.entities.comment',
  },
];
